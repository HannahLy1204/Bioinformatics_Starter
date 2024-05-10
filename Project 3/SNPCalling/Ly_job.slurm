#!/bin/bash -l
#SBATCH --account=lp_edu_large_omics
#SBATCH --clusters=wice
#SBATCH --partition=batch
#SBATCH --nodes=1
#SBATCH --ntasks=1
#SBATCH --cpus-per-task=8
#SBATCH --time=00:30:00
#SBATCH --mem=16G
#SBATCH --output=Workflow/SlurmLog/output_slurm.log
#SBATCH --error=Workflow/SlurmLog/error_slurm.log


#Student name: Thi Hanh Nguyen Ly

#Specify permission to execute Snakefile
export PATH=/lustre1/project/stg_00079/teaching/I0U19a_conda_2024/bin/:$PATH
chmod u+x Snakefile

#Create work folder
mkdir -p Workflow/
cp Snakefile Workflow/ 

#Running Snakemake
snakemake -c8 -s Workflow/Snakefile -d Workflow/
snakemake -c1 -s Workflow/Snakefile -d Workflow/ --report snake_report.html

echo "Slurm job is launched. Check error and output log for more information."