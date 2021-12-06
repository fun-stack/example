module "$name;format="snake"$" {
  source  = "fun-stack/fun/aws"
  version = "0.1.0"

  domain               = "$domain$"
  catch_all_forward_to = "$email$"

  website = {
    source_dir        = "../webapp/target/scala-2.13/scalajs-bundler/main/dist"
    cache_files_regex = ".*-hashed.(js|css)"
  }

  http = {
    source_dir  = "../lambda/target/scala-2.13/scalajs-bundler/main/dist"
    handler     = "main.handler"
    runtime     = "nodejs14.x"
    timeout     = 30
    memory_size = 256
    environment = {
      NODE_OPTIONS = "--enable-source-maps"
  }
  }

  auth = {
  }

  budget = {
    limit_monthly_dollar = "10.0"
    notify_email         = "$email$"
  }

  prod_workspace = "default"   # deployment to domain
  dev_workspaces = ["default"] # enables dev_setup, i.e. working locally
  dev_setup = {
    local_website_url = "http://localhost:12345" # auth can redirect to that website
    # local_http_host   = "localhost:8080" # http requests from the frontend go to this host instead of our http lambda
    # local_ws_host     = "localhost:8081" # ws requests from the frontend go to this host instead of our api lambda
  }
}

# resource "aws_iam_role_policy_attachment" "http" {
#   policy_arn = aws_iam_policy.http.arn
#   role       = module.$name;format="snake"$.http_role.name
# }

# resource "aws_iam_policy" "http" {
#   name = "\${module.$name;format="snake"$.prefix}-http-policy"
#   policy = <<EOF
# {
#     "Version": "2012-10-17",
#     "Statement": [
#         {
#             "Effect": "Allow",
#             "Action": [
#               "dynamodb:*"
#             ],
#             "Resource": [
#                 "\${aws_dynamodb_table.translation_cache.arn}"
#             ]
#         }
#     ]
# }
# EOF
#}
