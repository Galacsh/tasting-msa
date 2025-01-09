# Read key/value secrets
path "app/*" {
  capabilities = ["read", "list"]
}

# List existing secrets engines.
path "sys/mounts" {
  capabilities = ["read"]
}