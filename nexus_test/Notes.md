

### Resolve admin password
docker exec {container id} cat /nexus-data/admin.password

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
mvnw versions:set -DnewVersion=19.0.0
mvnw versions:set -DnewVersion=0.5.0-SNAPSHOT

### run tests default-deploy test-deploy
mvnw clean install -DskipTests=true
mvnw deploy -P '!maven-central-depl**oy,test-deploy' --settings nexus_test/settings.xml -DperformRelease=true -DskipTests=true

mvnw -P '!maven-central-depl**oy,test-deploy' --settings nexus_test/settings.xml -DperformRelease=true -DskipTests=true


###
https://blog.sonatype.com/four-steps-to-get-started-with-nexus-repo-using-new-rest-apis

