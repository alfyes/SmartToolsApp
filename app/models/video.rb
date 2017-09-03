class Video < ApplicationRecord
  belongs_to :concurso
  has_attached_file :sin_convertir
  #validates_attachment :image2, presence: true, content_type: { content_type: ["image/jpeg", "image/gif", "image/png"] }
  do_not_validate_attachment_file_type :sin_convertir
end
