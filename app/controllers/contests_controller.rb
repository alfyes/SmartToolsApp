class ContestsController < ApplicationController
  skip_before_action :authenticate_user!, only: [:index, :new, :create]

  def index
    custom_url_concurso = params[:urlconcurso]
    @concurso = Concurso.find_by_url(custom_url_concurso)
  end

  def new
    @video = Video.new
  end

  def create
    custom_url_concurso = params[:urlconcurso]
    concurso = Concurso.find_by_url(custom_url_concurso)
    @video = concurso.videos.new(video_params)
    if @video.save
      redirect_to '/contests/' + custom_url_concurso, notice: "The video #{@video.name} has been uploaded."
    else
      render 'new'
    end
  end

  private

  def video_params
    params.require(:video).permit(:name, :message, :firstNameUser, :lastNameUser, :emailUser, :sin_convertir)
  end
end
