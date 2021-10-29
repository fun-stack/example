# $name$

This is your whole application. All code in scala. Infrastructure as terraform code (see `terraform` directory).

## Initial steps

Create your s3-bucket for the terraform state:

```sh
aws s3 mb s3://$terraform_state_bucket$
```

Create a hosted zone in AWS for your custom domain. Either just register your domain in AWS directly or create a hosted zone in AWS for your domain (then set the nameservers at your registrar to the values you get from the following command):

```sh
aws route53 create-hosted-zone --name "$domain$" --caller-reference \$(date +%s)
```

These two steps only need to be done once.

If you need to register the nameservers with your own dns service you can list them again with these commands.

```sh
HOSTED_ZONE_ID=$(aws route53 list-hosted-zones | jq -r ".HostedZones[0].Id")
aws route53 get-hosted-zone --id $HOSTED_ZONE_ID | jq ".DelegationSet.NameServers"
```

## Deploy

Download the newest terraform version (currently 1.03): https://www.terraform.io/downloads.html

First build the application:

```sh
./build.sh
```

Then go into the terraform directory. Set your `AWS_PROFILE`. And run terraform:

```sh
terraform init -upgrade -reconfigure
terraform apply
```

Then the app is available under `https://$domain$`.

### Environments

If you want to try something out without interrupting others, you can make your own terraform workspace and setup your own independent deployment:

```sh
terraform workspace new <my-workspace>
terraform workspace switch <my-workspace>
# run terraform as usual
```

If you are not on the `default` terraform workspace, the app is available under: `https://<my-workspace>.env.$domain$`.

## Development

Deploy the app, then run:

```sh
sbt dev
```

Go to `http://localhost:12345` in your browser.

This will run against the environment according to the terraform workspace of the last deployment.
