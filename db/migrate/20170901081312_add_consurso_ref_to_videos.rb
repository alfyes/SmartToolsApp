class AddConsursoRefToVideos < ActiveRecord::Migration[5.1]
  def change
    add_reference :videos, :concurso, foreign_key: true
  end
end
