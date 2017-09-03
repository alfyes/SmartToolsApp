class AddAttachmentSinConvertirToVideos < ActiveRecord::Migration[5.1]
  def self.up
    change_table :videos do |t|
      t.attachment :sin_convertir
    end
  end

  def self.down
    remove_attachment :videos, :sin_convertir
  end
end
