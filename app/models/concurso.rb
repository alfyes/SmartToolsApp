class Concurso
  include Aws::Record
  include ActiveModel::Validations
  include ActiveModel::Conversion
  attr_accessor :user_id, :concurso_id, :url, :startDate, :endDate, :description,
                :image2, :name

  string_attr :user_id, hash_key: true
  string_attr :concurso_id, range_key: true
  string_attr :name
  string_attr :url
  datetime_attr :startDate
  datetime_attr :endDate
  string_attr :description
  string_attr :image2
end
