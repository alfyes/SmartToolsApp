class ConcursosController < ApplicationController

  def index
    @concursos = current_user.concursos
  end

  def show
    begin
      @concurso =  current_user.concursos.find(params[:id])
    rescue ActiveRecord::RecordNotFound
      redirect_to concursos_path
    end
  end

  def new
    @consurso = Concurso.new
  end

  def create
    @consurso = current_user.concursos.new(concurso_params)
    if @consurso.save
      redirect_to concursos_path, notice: "The video #{@consurso.name} has been uploaded."
    else
      render 'new'
    end
  end

  private

  def concurso_params
    params.require(:concurso).permit(:name, :url, :description, :image2, :startDate, :endDate)
  end
end
