# Crowdin Uploader

Command line tool to easily upload files with wildcard to Crowdin project.

## Build and Run

To build the app run gradle build task:
### cmd
```cmd
gradlew shadowJar
```

### shell
```shell
./gradlew shadowJar
```

To run the app add command line arguments:

```shell
java -jar build\libs\crowdin-uploader-all.jar "10" "token" "*.json"
```

## Arguments
### Project ID
First argument is project ID that consists of digits and can be found on the main page of the project.

### Token 
Second argument is Crowdin Personal Access Token that was generated under user API section.

### Wildcard
Third argument is a wildcard to find the files.

Can include `*` to specify one or more of any symbol.

The file extension can also include *.

The `.` between file name and extension is required.
Examples of the wilcard:
- `*.json`
- `file-*.json`
- `filenane*.*`

**The order of the arguments is important**

## Output
Output notify about the uploading process.
Possible output example with wildcard `na*.*`:
```shell
Start...
Found 2 files matching the wildcard.
Uploading name.txt...
File name.txt successfully uploaded!
Uploading naver.json...
File naver.json successfully uploaded!
Finish!
```

