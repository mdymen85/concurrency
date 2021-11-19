provider "aws" {
    region = "us-east-1"
}

#variable "ec2_names" {
#    type = list(string)
#    default = ["nginx","server1","server2","database","redis"]
#}

locals {
     ec2_instances = [
       {
            name = "nginx",
            ip_address = "172.31.16.4"
            
       },
       {
            name = "server1",
            ip_address = "172.31.16.5"       
       },
       {
            name = "server2",
            ip_address = "172.31.16.6"       
       },
       {
            name = "database",
            ip_address = "172.31.16.7"       
       },
       {
            name = "redis",
            ip_address = "172.31.16.8"       
       },                     
     
     ]

}

resource "aws_security_group" "terraform_segurity_group" {
     vpc_id = "vpc-06540f5331e97d85a"
     ingress {
       description      = "SSH"
       from_port        = 22
       to_port          = 22
       protocol         = "tcp"
       cidr_blocks      = ["0.0.0.0/0"]
     }   
     ingress {
       description      = "8081"
       from_port        = 8081
       to_port          = 8081
       protocol         = "tcp"
       cidr_blocks      = ["0.0.0.0/0"]
     }  
     ingress {
       description      = "8080"
       from_port        = 8080
       to_port          = 8080
       protocol         = "tcp"
       cidr_blocks      = ["0.0.0.0/0"]
     }                      
     tags = {
        Name = "security_group_tf"
     }
}

resource "aws_instance" "terraform_ec2_example" {
    #for_each = toset(var.ec2_names)
    for_each = {
       for index, vm in local.ec2_instances:
       index => vm
    }
    
    ami = "ami-083654bd07b5da81d"
    instance_type = "t2.micro"
    private_ip = each.value.ip_address
    vpc_security_group_ids = [
         aws_security_group.terraform_segurity_group.id
    ]
    provisioner "file" {
         source = "install-docker.sh"
         destination = "/tmp/install-docker.sh"
    }
    key_name   = "mdymen"    
    subnet_id = "subnet-056ceb1856e2d326e"
    tags = {
        Name = each.value.name
    }
  
    # Login to the ec2-user with the aws key.
    connection {
        type        = "ssh"
        user        = "ubuntu"
        private_key = file("~/.ssh/mdymen.pem")
        host        = self.public_ip
    }    
    user_data = <<-EOF
        #!/bin/bash
        sudo apt-get update
        sudo apt install docker -y      
        curl -sSL https://get.docker.com/ | sh
        sudo service docker start
        sudo usermod -a -G docker ubuntu
    EOF    
}


