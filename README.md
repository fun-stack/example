# Example

This is your whole application. All code in scala. Infrastructure as terraform code.

## Overview

Three scala projects:
- *lambda/*: backend code deployed as lambda function behind api gateway
- *webapp/*: frontend code deployed to s3 bucket behind cloudfront
- *api/*: shared code between lambda and webapp

Terraform deployment:
- *terraform/*: terraform code deploy backend and frontend to AWS.

## Requirements

- aws-cli
- sbt
- yarn
- node (>= 10.13.0)
- terraform (>= 1.0.0): https://www.terraform.io/downloads.html

## Development

Compile the application and run a development webserver (it automatically watches for changes):
```sh
sbt dev
```

Go to `http://localhost:12345` in your browser. You will see your app there.

Additionally, run your backend lambda locally (it automatically watches for changes):

```
yarn install
```

```
npx lambda-server http lambda/target/scala-2.13/scalajs-bundler/main/lambda-fastopt.js handlerHttp &
npx lambda-server ws lambda/target/scala-2.13/scalajs-bundler/main/lambda-fastopt.js handlerWs &
```

Now, the frontend can call the http server. Feel free to try and change your code and see how the page updates automatically.

#### Infos about webapp

Webpack configuration: `webapp/webpack.config.dev.js`, `webapp/webpack.config.prod.js`

Postcss configuration (picked up by webpack): `webapp/postcss.config.js`

Tailwind configuration (picked up by postcss): `webapp/tailwind.config.js`

Template for index.html and css (picked up by webpack): `webapp/src/main/html/index.html`, `webapp/src/main/css/`

Static assets folder (picked up by webpack): `webapp/assets/`

Local development configuration for your webapp: `webapp/local/app_config.js`

#### Infos about lambda

Webpack configuration: `lambda/webpack.config.dev.js`, `lambda/webpack.config.prod.js`

## Deployment

### Initial steps

You have to do these steps only once.

Create an s3-bucket and dynamodb table for the terraform state (generates a `terraform/terraform.tf` file):

```sh
# export AWS_PROFILE=<my-profile>
./initial_setup.sh
# git add terraform/terraform.tf
```

#### If you have a custom domain

Set your `domain` in `terraform/fun.tf`.

Create a hosted zone in AWS for this custom domain.
Either just register your domain in AWS directly - then you do not need to do anything else here.
Or create a hosted zone in AWS for your already owned domain:

```sh
aws route53 create-hosted-zone --name "example.com" --caller-reference $(date +%s)
```

For your already owned domain, you need point your domain registrar to the nameservers of this hosted zone. You need to do this where you bought the domain. Here is a command to print the nameserver IPs of the hosted zone again:

```sh
HOSTED_ZONE_ID=$(aws route53 list-hosted-zones-by-name --dns-name "example.com" | jq -r ".HostedZones[0].Id")
aws route53 get-hosted-zone --id $HOSTED_ZONE_ID | jq ".DelegationSet.NameServers"
```

### Deploy steps

First build the application:

```sh
sbt prod
```

Then go into the terraform directory. Set your `AWS_PROFILE`. And run terraform:

```sh
# set your AWS_PROFILE
terraform init -upgrade -reconfigure
terraform apply
```

Then the app is available under `https://example.com`.
Without a custom domain, you can see the endpoint in the outputs of the apply command.

### Environments

If you want to try something out without interrupting others, you can make your own terraform workspace and setup your own independent deployment:

```sh
terraform workspace new <my-workspace>
terraform workspace switch <my-workspace>
# run terraform as usual
```

If you are not on the `default` terraform workspace, the app is available under: `https://<my-workspace>.env.example.com`.
