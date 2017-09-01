class Concurso < ApplicationRecord
  belongs_to :user
  has_many :videos, dependent: :destroy
  has_attached_file :image2
  #validates_attachment :image2, presence: true, content_type: { content_type: ["image/jpeg", "image/gif", "image/png"] }
  do_not_validate_attachment_file_type :image2
end
