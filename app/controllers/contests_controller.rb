class ContestsController < ApplicationController
  include ContestsHelper
  skip_before_action :authenticate_user!, only: [:index, :new, :create]
  layout "videos_layout"

  def index
    custom_url_concurso = params[:urlconcurso]
    @concurso = Concurso.query(index_name: 'ConcursoXUrl',
                               key_condition_expression: 'concurso_url = :h',
                               expression_attribute_values: { ':h' => custom_url_concurso }).first()
    # @videos = @concurso.videos.reverse_order.paginate(:page => params[:page], :per_page => 3)
    @videos = Video.query(key_condition_expression: 'concurso_id = :h',
                          expression_attribute_values: { ':h' => @concurso.concurso_id })
  end

  def new
    custom_url_concurso = params[:urlconcurso]
    # @concurso = Concurso.find_by_url(custom_url_concurso)
    @concurso = Concurso.query(index_name: 'ConcursoXUrl',
                               key_condition_expression: 'concurso_url = :h',
                               expression_attribute_values: { ':h' => custom_url_concurso }).first()
    @video = Video.new
  end

  def create
    custom_url_concurso = params[:urlconcurso]
    concurso = Concurso.query(index_name: 'ConcursoXUrl',
                              key_condition_expression: 'concurso_url = :h',
                              expression_attribute_values: { ':h' => custom_url_concurso }).first()
    @video = Video.new(video_params)
    @video.fileName = upload_video
    @video.createDate = DateTime.now
    @video.video_id = generate_video_uuid
    @video.concurso_id = concurso.concurso_id
    @video.state = 0
    @video.fileNameConv = ''

    if @video.save
      redirect_to '/contests/' + custom_url_concurso, notice: "The video #{@video.name} has been uploaded."
    else
      render 'new'
    end
  end

  private

  def video_params
    params.require(:video).permit(:name, :message, :firstNameUser,
                                  :lastNameUser,
                                  :emailUser, :fileName)
  end

  def upload_video
    uploaded_io = params[:video][:fileName]

    nombre_video = get_video_name(uploaded_io.original_filename)

    File.open(Rails.root.join('public', 'system', 'videos', 'original',
                              nombre_video), 'wb') do |file|
      file.write(uploaded_io.read)
    end
    nombre_video
  end
end
