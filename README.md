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
- lambda-server: yarn add --global @fun-stack/lambda-server

## Development

Compile the application and run a development webserver (it automatically watches for changes):
```sh
sbt dev
```

Go to `http://localhost:12345` in your browser. You will see your app there.

Additionally, run your backend lambda locally (it automatically watches for changes):

```
lambda-server http lambda/target/scala-2.13/scalajs-bundler/main/lambda-fastopt.js handlerHttp &
lambda-server ws lambda/target/scala-2.13/scalajs-bundler/main/lambda-fastopt.js handlerWs &
wait
```

Now, the frontend can call the http server. Feel free to try and change your code and see how the page updates automatically.

Alternatively, you can run your frontend against the real deployed backend: see file `web-client/webpack.config.dev.js`.

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

Create your s3-bucket for the terraform state (it needs to be a globally unique name, you can still change it in `terraform/terraform.tf`):

```sh
# set your AWS_PROFILE
./initial_setup.sh
```

#### If you have a custom domain

Set `domain = "example.com"` in `terraform/fun.tf`.

Create a hosted zone in AWS for this custom domain.
Either just register your domain in AWS directly and be done.
Or create a hosted zone in AWS for your domain (then set the nameservers at your registrar to the values you get from the following command):

```sh
aws route53 create-hosted-zone --name "example.com" --caller-reference $(date +%s)
```

If you need to register the nameservers with your own dns service you can list them again with these commands:

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
