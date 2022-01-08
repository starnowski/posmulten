Project used for conversion of markdown files to ansi format.

### Usage
```shell script
node convert.js {PATH TO README.md file} {PATH TO output file}
```

### Example

```shell script
node convert.js README.md test.txt
```

### Docker image
Due to the apparent differences between the generated files on Windows and Linux OS systems the recommended approach is to generate file on Linux system.
If a project contributor does not have installed Linux system then he can use provisioned Dockerfile to create docker image with project and required dependencies.
Dockerfile can be found in the configuration-yaml-interpreter-readme-converter directory.
Below there are instruction how the image can be build and run:

```shell
# Current directory is configuration-yaml-interpreter-readme-converter
docker build -t markdown_converter .
export README_FILE_DIRECTORY="{path to directory where README.md file can be found}"
docker run --name ansible_server_bats_test -v "${README_FILE_DIRECTORY}:/test_files" --rm markdown_converter node convert.js "/test_files/README.md" "/test_files/generated_output"
# Print converted README.md file
cat "${README_FILE_DIRECTORY}/generated_output"
```
