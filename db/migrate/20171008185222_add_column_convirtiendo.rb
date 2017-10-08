class AddColumnConvirtiendo < ActiveRecord::Migration[5.1]
  def change
    add_column :videos, :convirtiendo, :boolean
  end
end
