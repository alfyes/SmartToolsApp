class ConcursosController < ApplicationController

  def index
    @concursos = current_user.concursos
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

  def edit
    @consurso = Concurso.find(params[:id])
  end

  def update
    @consurso = Concurso.find(params[:id])

    if @consurso.update(concurso_params)
      redirect_to concursos_path
    else
      render 'edit'
    end
  end

  def destroy
    @concurso = Concurso.find(params[:id])
    @concurso.destroy

    redirect_to concursos_path
  end

  private

  def concurso_params
    params.require(:concurso).permit(:name, :url, :description, :image2, :startDate, :endDate)
  end


end
