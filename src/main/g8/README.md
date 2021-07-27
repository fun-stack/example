# update.fm

## Development

Deploy the app, then run:
```sh
sbt dev
```

Go to `http://localhost:12345` in your browser.

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

If you want to try something out without interrupting others, you can make your own terraform workspace and setup your own independent deployment:
```sh
terraform workspace new <my-workspace>
terraform workspace switch <my-workspace>
# run terraform as usual
```

If you are not on the `default` terraform workspace, the app is available under: `https://<my-workspace>.env.$domain$`.
