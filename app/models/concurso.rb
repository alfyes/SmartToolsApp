class Concurso
  include Aws::Record
  include ActiveModel::Validations
  include ActiveModel::Conversion
  attr_accessor :user_id, :concurso_id, :concurso_url, :startDate, :endDate, :description,
                :image2, :name

  set_table_name 'Concursos'

  string_attr :user_id, hash_key: true
  string_attr :concurso_id, range_key: true
  string_attr :name
  string_attr :concurso_url
  datetime_attr :startDate
  datetime_attr :endDate
  string_attr :description
  string_attr :image2

  global_secondary_index(:ConcursoXUrl, hash_key: :concurso_url, projection: { projection_type: 'ALL' })

end
