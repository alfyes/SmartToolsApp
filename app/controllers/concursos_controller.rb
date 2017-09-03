class ConcursosController < ApplicationController
  def new
    @consurso = Concurso.new
  end

  def create
    @consurso = Concurso.new(concurso_params)
    @usuario = User.new
    @usuario.id = 1
    @consurso.user = @usuario
    if @consurso.save
      redirect_to home_index_path, notice: "The video #{@consurso.name} has been uploaded."
    else
      render 'new'
    end
  end

  private

  def concurso_params
    params.require(:concurso).permit(:name, :url, :description, :image2, :startDate, :endDate)
  end
end
