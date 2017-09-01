class CreateConcursos < ActiveRecord::Migration[5.1]
    create_table :concursos do |t|
      def change
      t.string :name
      t.string :image
      t.string :url
      t.date :startDate
      t.date :endDate
      t.text :description

      t.timestamps
    end
  end
end
