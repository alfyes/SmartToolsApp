require 'aws-sdk'

Aws.config[:dynamodb] = { endpoint: 'http://localhost:8000' }

# Crea la tabla de concursos
unless Concurso.table_exists?
  migration = Aws::Record::TableMigration.new(Concurso)
  migration.create!(provisioned_throughput: { read_capacity_units: 5,
                                              write_capacity_units: 2 })
  migration.wait_until_available
end