
### Resolve admin password
docker exec ece9d5836ff9 cat /nexus-data/admin.password

### Project user

projectUser
John
Doe
john.doe@email.com
xxx

## Create mavenRelease role
nx-repository-admin-maven2-maven-releases-*
nx-repository-view-maven2-maven-releases-*


### test
mvn versions:set -DnewVersion=19.0.0

### run tests default-deploy test-deploy
mvn deploy -P '!maven-central-deploy,test-deploy' --settings nexus_test/settings.xml -DperformRelease=true -DskipTests=true


