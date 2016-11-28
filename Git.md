###Git

#### 基础知识
git的文件有三种状态：已修改（modified）、已暂存（staged）、已提交（committed）。  

#### 命令
git log --graph --all --decorate ：显示所有分支提交详情，有冲突比较。    

git merge xxx2 : merge分支，将xxx2merge到当前所在的分支上。

合并失败：
git status : 查看冲突文件  
解决冲突  
git add :解决后add   
git commit : 提交  


git branch -v : 参数效果是可以显示每个分支的最近一次提交。  

git branch --merged : 可以过滤只显示已经合并到当前分支的分支。  
git branch --no-merged : 可以过滤只显示未合并到当前分支的分支。  


git branch -d xxx :删除xxx分支。如果分支还有未合并的修改，不让删除的。  
git branch -D xxx :强制删除xxx分支。   


git push (remote) (branch) : 推送  仓库  分支    
相当于  
git push (remote) (本地branch名：远程branch名)：用这种写法可以将本地分支推到名字不同的远程分支。    


git branch -vv:可以显示当前所有本地分支的跟踪详情。跟踪的哪个远程分支，当前状态是领先还是落后都有。    

git push origin --delete 远程分支名。：删除远程分支。    


变基rebase，一两句说不清楚。用merge好了。    


    




