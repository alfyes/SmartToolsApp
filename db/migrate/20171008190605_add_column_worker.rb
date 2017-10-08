class AddColumnWorker < ActiveRecord::Migration[5.1]
  def change
    add_column :videos, :worker, :integer
  end
end
