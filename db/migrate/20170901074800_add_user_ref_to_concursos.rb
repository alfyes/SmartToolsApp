class AddUserRefToConcursos < ActiveRecord::Migration[5.1]
  def change
    add_reference :concursos, :user, foreign_key: true
  end
end
