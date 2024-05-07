#Loading library
library(ggplot2)
library(GGally) 
library(FactoMineR)
library(patchwork)
library(dplyr)
library(factoextra)
library(pracma)
library(caret)

#Loading data
Dia_data <- read.table("C:/Users/hanhn/Desktop/KUL_Master/AMSA/Dataset of Diabetes.csv",sep=',', header=T)
Dia1 <- Dia_data[, 3:14]                        #Skipping the first 2 id_columns
Dia1$Gender <- ifelse(Dia1$Gender == "M", 0, 1) #Convert "Gender" column to 0 and 1
Dia1$CLASS <- trimws(Dia1$CLASS)                #Trimming all space characters in CLASS column
attach(Dia1)

#PREPROCESSING
any(is.na(Dia1)) #Checking for missing values
sum(is.na(Dia1)) #There is no missing values

predict_CLASS <- Dia1[CLASS=="P", ] #Storing subset values

summary(Dia1)
dim(Dia1)
corr_matrix <- cor(Dia1[, 1:11]) #Correlation matrix

#DATA VISUALIZATION
Dia1$CLASS <- factor(CLASS)
attach(Dia1)
Dia2 <- Dia1[, 1:11]
var_names <- names(Dia2)
plots <- list()               #List storing all density plots
#Density plots
for (var_name in names(Dia2)) {
  p <- ggplot(Dia2, aes(x = .data[[var_name]], fill = CLASS)) +
    geom_density(alpha = 0.5) +
    labs(title = paste("Density plot of", var_name))
  
  plots[[var_name]] <- p      #Store the plot in the list
}
wrap_plots(plots, ncol = 3)

#Pairwise plot for each variable (except for the dependent variable CLASS)
Dia1_numeric <- apply(Dia1[, 1:11], 2, as.numeric)
ggpairs(data.frame(Dia1_numeric),main='Paired scatterplots of dataset') 
#Take a few seconds

#AndrewPlot
andrewsplot(Dia1_numeric, f=Dia1$CLASS, style = "cart")
legend("topright", legend = unique(CLASS),  col = c("green", "red", "blue"),
       lty=1)

#STANDARDIZATION
standardize_func <- function(x)
{ z <- (x - mean(x)) / sd(x) 
return( z)} 
Dia_Z <- apply(Dia1[1:11], 2, standardize_func)
#PCA ANALYSIS
Dia_PCA <- PCA(Dia_Z, graph=T)            #Dia_Z already standardized

fviz_pca_var(Dia_PCA, col.var = "cos2",
             gradient.cols = c("#0018F9", "#FC4E07"), 
             repel = TRUE)

fviz_pca_ind(Dia_PCA,
             geom.ind = "point",
             col.ind = Dia1$CLASS, 
             palette = c("#0018F9", "#FF00FF", "#FC4E07"),
             addEllipses = TRUE, 
             legend.title = "Groups")

fviz_pca_biplot(Dia_PCA, 
                # Individuals
                geom.ind = "point",
                fill.ind = Dia1$CLASS, col.ind = "black",
                pointshape = 21, pointsize = 2,
                palette = "jco",
                addEllipses = TRUE,
                # Variables
                alpha.var ="contrib", col.var = "contrib",
                gradient.cols = c("#0018F9", "#FF00FF", "#FC4E07"),
                
                legend.title = list(fill = "CLASS", color = "Contrib",
                                    alpha = "Contrib"))

Eigen <- t(Dia_PCA$eig)
eigen_Kaiser <- Eigen[,Eigen[1,] > 1]
df <- data.frame(Dia_PCA$eig)
plot(df$eigenvalue, type = 'h', main = "Eigenvalues vs Components", 
     xlab = "PCs", ylab = "Eigenvalues", lwd=1.5)
abline(h=1.0)
text(8, 1.07, "Kaiser rule")

#CLUSTERING - Hierarchical clustering
#Clustering on principle components
Dia.hcpc<-HCPC(Dia_PCA,graph=TRUE, nb.clust=3)
plot.HCPC(Dia.hcpc, choice = 'tree', ind.names = F)
plot.HCPC(Dia.hcpc, axes = 1:2)
Dia.hcpc$desc.var

cluster <- data.frame(Dia.hcpc$data.clust)
cluster %>% group_by(clust) %>% summarize(Patients_count = n())
df = data.frame(Dia.hcpc$call$X)
ggplot(df, aes(Dim.1, Dim.2))+geom_point(aes(col = clust))+theme_bw()+
  stat_ellipse(geom="polygon", aes(fill = clust), alpha = 0.2, show.legend = F)

#Clustering directly on data
Dia_tree <- hcut(Dia1[, 1:11], k = 3, stand = TRUE)
fviz_dend(Dia_tree, rect = TRUE,cex = 0.3,k_colors = "jco",tittle = "Lower",
          ggtheme = theme_classic(), graph=T) #Take some time
#Comparing cluster and real data CLASS (diabetic classification)
cluster$id <- 1:nrow(cluster)
Dia1$id <- 1:nrow(Dia1)
result <- inner_join(cluster, Dia1, by = "id") %>%
  select(id, CLASS, clust)

count_table <- table(result$CLASS, result$clust)
count_table
barplot(count_table,legend.text = TRUE, beside = TRUE,
        main = "Does clustering match real classification",
        xlab = "Cluster",
        ylab = "Counts", col=c("#66CCFF", "#6699FF", "#3366FF"))

result <- result %>%
  mutate(presicted_value = recode(clust, "1"= "N", "3" = "P", "2" = "Y"))
confusion <- confusionMatrix(data=factor(result$presicted_value), reference =
                               factor(result$CLASS))
confusion
