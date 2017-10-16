require 'aws-sdk'

no_sql_remoto = ENV['NO_SQL_HOST']

if no_sql_remoto.nil? || !no_sql_remoto.casecmp?('remoto')
  Aws.config[:dynamodb] = { endpoint: 'http://localhost:8000' }
end
# Crea la tabla de concursos
unless Concurso.table_exists?
  migration = Aws::Record::TableMigration.new(Concurso)
  migration.create!(provisioned_throughput: { read_capacity_units: 5,
                                              write_capacity_units: 2 },
                    global_secondary_index_throughput:
                        { ConcursoXUrl: { read_capacity_units: 5,
                                          write_capacity_units: 2 } })
  migration.wait_until_available
end

# Crea la tabla de Videos
unless Video.table_exists?
  migration = Aws::Record::TableMigration.new(Video)
  migration.create!(provisioned_throughput: { read_capacity_units: 5,
                                              write_capacity_units: 2 })
  migration.wait_until_available
end
