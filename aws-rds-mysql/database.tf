resource "aws_db_instance" "nequi_franquicias" {
  engine                 = "mysql"
  engine_version         = "8.4"
  instance_class         = var.db_instance_class
  allocated_storage      = 10
  db_name                = var.db_name
  username              = var.db_username
  password              = var.db_password
  parameter_group_name   = "default.mysql8.4"
  publicly_accessible    = true
  skip_final_snapshot    = true
}
