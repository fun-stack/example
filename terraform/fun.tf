locals {
  is_prod = terraform.workspace == "default"
}

module "example" {
  source  = "fun-stack/fun/aws"
  version = "0.4.0"

  stage = terraform.workspace

  # domain = {
  #   name                = "example.com"
  #   deploy_to_subdomain = local.is_prod ? null : "${terraform.workspace}.env"
  #   catch_all_email     = "me@example.net"
  # }

  website = {
    source_dir        = "../webapp/target/scala-2.13/scalajs-bundler/main/dist"
    cache_files_regex = ".*-hashed.(js|css)"
    rewrites = {
      "robots.txt" = "robots.deny.txt" # local.is_prod ? "robots.allow.txt" : "robots.deny.txt"
    }
  }

  http = {
    source_dir            = "../lambda/target/scala-2.13/scalajs-bundler/main/dist"
    handler               = "main.handlerHttp"
    runtime               = "nodejs14.x"
    timeout               = 30
    memory_size           = 256
    allow_unauthenticated = true
    environment = {
      NODE_OPTIONS = "--enable-source-maps"
    }
  }

  ws = {
    source_dir            = "../lambda/target/scala-2.13/scalajs-bundler/main/dist"
    handler               = "main.handlerWs"
    runtime               = "nodejs14.x"
    timeout               = 30
    memory_size           = 256
    allow_unauthenticated = true
    environment = {
      NODE_OPTIONS = "--enable-source-maps"
    }
  }

  auth = {
  }

  # budget = {
  #   limit_monthly_dollar = "10.0"
  #   notify_email         = "me@example.net"
  # }

  # dev_setup = {
  #   enabled = !local.is_prod
  #   local_website_url = "http://localhost:12345" # auth can redirect to that website
  # }
}
