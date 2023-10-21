# Gitlet Design Document

## Classes and Data Structures

### Commit

#### Instance Variables

+ String message
+ String parent
+ Map fileSnapshot
+ String commitID
+ String date
+ Logs
  + int add
  + int delete
  + int modify



### Repository

#### Functions

+ Commit createNewCommit(String message) -> getCommitID()&Commit.inheritParentMap()

+ void createSnapshotOfFile(Commit commit) -> Commit.addOrModifyMapKV()
+ Commit getCurrentCommit() -> getCommitID()
+ Commit getSpecifiedCommit(String id)
+ String getCommitID(File file)
+ void changeHead(Commit commit)
+ void commitPersistence(Commit commit)
+ hasFileInCommit(Commit commit, String filename) -> Commit.hasFile()
+ hasSameSnapshot(Commit commit, String filename) -> Commit.getFileSnapshot()
+ List\<Commit\> getCommits()
+ String getCurrentBranch()
+ void printBranches() -> getCurrentBranch()
+ void printFiles(File folder)
+ void printComparedFilesInAddandCWD()
+ void printComparedFilesInCommitAndCWD() -> getCurrentCommit() & Commit.getFileSnapshot() & isStaged() & printComparedFilesInAddandCWD()
+ void printUntrackedFiles() -> isStaged() & isCommitted()
+ boolean isUntrackedFileExist() -> isStaged() & isCommitted()
+ isStaged(File area, String filename)
+ isCommitted(String filename) -> getCurrentCommit() & Commit.hasFileComparedToCWD(filename)
+ delelteRedundantFiles(String commitID) -> getSpecifiedCommit() & Commit.getFileSnapshot()
+ 




## Algorithms

## Persistence

```c
     *  .gitlet
     *  |---objects
     *  |    |---commits
     *  |    |---(hash of filename)
     *  |        |---snapshot of files
     *  |---area
     *  |   |---add
     *  |   |---remove
     *  |---branches
     *  |---HEAD
```

