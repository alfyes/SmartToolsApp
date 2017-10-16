class Video
  include Aws::Record
  include ActiveModel::Validations
  include ActiveModel::Conversion
  attr_accessor :concurso_id, :video_id, :name, :message, :firstNameUser,
                :lastNameUser, :emailUser, :state, :createDate, :fileName,
                :fileNameConv

  set_table_name 'Videos'

  string_attr :concurso_id, hash_key: true
  string_attr :video_id, range_key: true
  string_attr :name
  string_attr :message
  string_attr :firstNameUser
  string_attr :lastNameUser
  string_attr :emailUser
  integer_attr :state
  datetime_attr :createDate
  string_attr :fileName
  string_attr :fileNameConv

end
