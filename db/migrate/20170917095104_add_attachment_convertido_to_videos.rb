class AddAttachmentConvertidoToVideos < ActiveRecord::Migration[5.1]
  def self.up
    change_table :videos do |t|
      t.attachment :convertido
    end
  end

  def self.down
    remove_attachment :videos, :convertido
  end
end
