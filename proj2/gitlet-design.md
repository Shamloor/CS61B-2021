# Gitlet Design Document

**Name**: Willson Yu

## Classes and Data Structures

### Repository

#### Instance Variables

* CWD - current working directory.
* GITLET_DIR - .gitlet directory containing all hidden data.
* HEAD - head pointer.
* OBJECTS - object folder containing blobs and commits.
* ADDAREA - Add area in staging area.
* REMOVEAREA - Remove area in staging area.

### TrackedFiles

#### Instance Variables

* TrackedFiles - Set for names of tracked files.


### StagingArea

#### Instance Variables

* BeRemoved - whether a file can be removed.


### Commit

#### Instance Variables

* Message - contains the message of a commit.
* Timestamp - time when a commit was created. Assigned by the constructor.
* Parent - the parent file name of a commit object.
* CommitID - Uses SHA1 function to create unique ID.
* FileToBlob - Store the file version, and points to files in blobs.

#### Functions
* 

### Blobs

#### Instance Variables

* FolderName - every folder stores blobs of one file.
* BlobID - the filename of one blob which was made by SHA1 function.
* ModifyInfo - information about modification.

## Algorithms

## Persistence

