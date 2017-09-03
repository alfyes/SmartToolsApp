class CreateVideos < ActiveRecord::Migration[5.1]
  def change
    create_table :videos do |t|
      t.string :name
      t.text :message
      t.string :firstNameUser
      t.string :lastNameUser
      t.string :emailUser
      t.boolean :state, default: false

      t.timestamps
    end
  end
end
