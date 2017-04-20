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

-----------
#### 在代码还没有提交时，巧用stash切换当前工作的分支
有时候写着写着代码，发现自己根本不在想要工作的分支上面啊。。。这时想用 `checkout [new branch]` 切换到期望的分支[new branch]上时，可能提示“你的本地更改会因分支变更而被覆盖”，从而无法checkout。

**这时候难道要硬着头皮提交自己的半成品代码然后再merge吗？NO！下面介绍一下git stash 命令**
> git stash: 备份当前的工作区的内容，从最近的一次提交中读取相关内容，让工作区回退到上次提交的内容。同时，将当前的工作区内容保存到Git栈中。
> 
> git stash pop: 从Git栈中读取最近一次保存的内容，恢复工作区的相关内容。由于可能存在多个Stash的内容，所以用栈来管理，pop会从最近的一个stash中读取内容并恢复。
> 
> git stash list: 显示Git栈内的所有备份，可以利用这个列表来决定从那个地方恢复。
> 
> git stash clear: 清空Git栈。此时使用gitg等图形化工具会发现，原来stash的哪些节点都消失了。

可以看到git stash可以解决我们遇到的问题
> 在工作区根目录备份当前未提交的代码
> 
>     git stash
> 切换到目标分支
> 
>     git checkout
> 此时应该就可以顺利切换到新的分支，再将之前备份的改动pop出来
> 
>     git stash pop

这样一来就可以实现未提交代码迁移到另外一个分支上去  


------
#### 批量删除本地分支
借助linux的管道方式  
> git branch | grep 'bran' | xargs git branch -d

