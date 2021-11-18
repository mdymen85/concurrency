provider "aws" {
    region = "us-east-1"
}

variable "ec2_names" {
    type = list(string)
    default = ["nginx","server1","server2","database","redis"]

}

resource "aws_instance" "terraform_ec2_example" {
    for_each = toset(var.ec2_names)
    ami = "ami-083654bd07b5da81d"
    instance_type = "t2.micro"
    tags = {
        Name = each.value
    }
}


