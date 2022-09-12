terraform {
  backend "s3" {
    encrypt        = true
    region         = "eu-central-1"
    key            = "fun/my-app.tfstate"
    bucket         = "373988118548-terraform-state"
    dynamodb_table = "terraform-lock"
  }
}

provider "aws" {
  region = "eu-central-1"
}
