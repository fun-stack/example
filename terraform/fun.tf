locals {
  is_prod = terraform.workspace == "default"
}

resource "aws_security_group" "test" {
  name   = "fun-sg"
  vpc_id = module.vpc.vpc_id

  egress {
    from_port        = 0
    to_port          = 0
    protocol         = "-1"
    cidr_blocks      = ["0.0.0.0/0"]
    ipv6_cidr_blocks = ["::/0"]
  }
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "3.14.0"

  name               = "fun-vpc"
  cidr               = "10.10.0.0/16"
  azs                = jsondecode("[\"eu-central-1a\",\"eu-central-1b\",\"eu-central-1c\"]")
  private_subnets    = jsondecode("[\"10.10.1.0/24\",\"10.10.2.0/24\",\"10.10.3.0/24\"]")
  public_subnets     = jsondecode("[\"10.10.101.0/24\",\"10.10.102.0/24\",\"10.10.103.0/24\"]")
  enable_nat_gateway = true
}

module "example" {
  source  = "fun-stack/fun/aws"
  version = "0.6.2"

  stage = terraform.workspace

  # domain = {
  #   name                = "example.com"
  #   deploy_to_subdomain = local.is_prod ? null : "${terraform.workspace}.env"
  #   catch_all_email     = "me@example.net"
  # }

  website = {
    source_dir              = "../webapp/target/scala-2.13/scalajs-bundler/main/dist"
    cache_files_regex       = ".*-hashed.(js|css)"
    content_security_policy = "default-src 'self'; connect-src https://* wss://*; frame-ancestors 'none'; frame-src 'none';"
    rewrites = {
      "robots.txt" = "robots.deny.txt" # local.is_prod ? "robots.allow.txt" : "robots.deny.txt"
    }
  }

  http = {
    api = {
      source_dir  = "../lambda/target/scala-2.13/scalajs-bundler/main/dist"
      handler     = "main.httpApi"
      runtime     = "nodejs14.x"
      memory_size = 256
      environment = {
        NODE_OPTIONS = "--enable-source-maps"
      }
      vpc_config = {
        subnet_ids         = module.vpc.private_subnets
        security_group_ids = [aws_security_group.test.id]
      }
    }

    rpc = {
      source_dir  = "../lambda/target/scala-2.13/scalajs-bundler/main/dist"
      handler     = "main.httpRpc"
      runtime     = "nodejs14.x"
      memory_size = 256
      environment = {
        NODE_OPTIONS = "--enable-source-maps"
      }
    }
  }

  ws = {
    rpc = {
      source_dir  = "../lambda/target/scala-2.13/scalajs-bundler/main/dist"
      handler     = "main.wsRpc"
      runtime     = "nodejs14.x"
      memory_size = 256
      environment = {
        NODE_OPTIONS = "--enable-source-maps"
      }
    }

    event_authorizer = {
      source_dir  = "../lambda/target/scala-2.13/scalajs-bundler/main/dist"
      handler     = "main.wsEventAuth"
      runtime     = "nodejs14.x"
      memory_size = 256
      environment = {
        NODE_OPTIONS = "--enable-source-maps"
      }
    }
  }

  auth = {
    image_file = "auth.jpg"
    css_file   = "auth.css"
  }

  # budget = {
  #   limit_monthly_dollar = "10.0"
  #   notify_email         = "budget@example.com"
  # }

  # dev_setup = {
  #   # enabled           = !local.is_prod
  #   local_website_url = "http://localhost:12345" # auth can redirect to that website, cors of http api allows origin
  # }
}
