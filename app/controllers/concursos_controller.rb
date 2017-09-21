class ConcursosController < ApplicationController

  def index
    @concursos = current_user.concursos.reverse_order.paginate(:page => params[:page], :per_page => 4)
  end

  def show
    begin
      @concurso =  current_user.concursos.find(params[:id])
      @videos = @concurso.videos.reverse_order.paginate(:page => params[:page], :per_page => 4)
    rescue ActiveRecord::RecordNotFound
      redirect_to concursos_path
    end
  end

  def new
    @consurso = Concurso.new
  end

  def create
    @consurso = current_user.concursos.new(concurso_params)
    @consurso.url = @consurso.name.gsub(/[^a-zA-Z\d_\-]/, '-')
    if @consurso.save
      redirect_to concursos_path, notice: "El concurso #{@consurso.name} ha sido creado."
    else
      render 'new'
    end
  end

  def edit
    @consurso = Concurso.find(params[:id])
  end

  def update
    @consurso = Concurso.find(params[:id])
    if @consurso.update(concurso_params_update)
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

  def concurso_params_update
    params.require(:concurso).permit(:name, :url, :description, :image2, :startDate, :endDate)
  end
  def concurso_params
    params.require(:concurso).permit(:name, :description, :image2, :startDate, :endDate)
  end


end
