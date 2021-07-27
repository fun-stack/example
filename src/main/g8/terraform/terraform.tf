terraform {
  backend "s3" {
    encrypt = true
    region  = "eu-central-1"
    key     = "$name;format="snake"$.tfstate"
    bucket  = "$terraform_state_bucket$"
  }
}

provider "aws" {
  region = "eu-central-1"
}
