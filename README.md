##### Builds the client
`mvn clean install`

##### Runs the client
`java -jar target/snapper-client-0.1.0-SNAPSHOT.jar --filename=/path/to/file`

- Required Parameters
  - `--filename` the full path to the file that contains the data

- Optional Parameters
  - `--server` hostname/ip of the server (default _localhost_)
  - `--port` port where the service is exposed (default _8080_)
