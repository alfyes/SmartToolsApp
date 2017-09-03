class AddAttachmentImageToConcursos < ActiveRecord::Migration[5.1]
  def self.up
    change_table :concursos do |t|
      t.attachment :image2
    end
  end

  def self.down
    remove_attachment :concursos, :image2
  end
end
