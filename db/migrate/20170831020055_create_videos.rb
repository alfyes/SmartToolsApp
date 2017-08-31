class CreateVideos < ActiveRecord::Migration[5.1]
  def change
    create_table :videos do |t|
      t.string :name
      t.string :ruta
      t.string :tipo

      t.timestamps
    end
  end
end
