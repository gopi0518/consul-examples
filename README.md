# consul-examples
* Install and start the consul agent locally
  https://learn.hashicorp.com/tutorials/consul/get-started-install?in=consul/getting-started
* Register Confluent cluster service healthchecks with consul
  curl --request PUT --data @ccloudhealth_primary.json http://localhost:8500/v1/agent/service/register
* Start Springboot application
* Access http://localhost:8081/get from browser, message will be published to health kafka cluster
* Testing cluster fail scenarios

** Change the health check endpoint in "ccloudhealth_primary.json", to fail the health check.
** Access this url "http://localhost:8081/get" from browser, now the message will be published to a secondary(fail-over cluster).
