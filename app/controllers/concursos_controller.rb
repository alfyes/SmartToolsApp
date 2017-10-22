class ConcursosController < ApplicationController
  include ConcursosHelper
  def index

    # @concursos = current_user.concursos.reverse_order.paginate(:page => params[:page], :per_page => 4)
    @concursos = Concurso.query(key_condition_expression: 'user_id = :h',
                                expression_attribute_values: { ':h' => current_user.email })

  end

  def show
    @concurso = Concurso.find(user_id: current_user.email, concurso_id: params[:id])
    #@videos = @concurso.videos.reverse_order.paginate(page: params[:page], per_page: 4)
    @videos = Video.query(index_name: 'VideosXFecha', key_condition_expression: 'concurso_id = :h',
                          expression_attribute_values: { ':h' => params[:id] })
  rescue ActiveRecord::RecordNotFound
    redirect_to concursos_path
  end

  def new
    @consurso = Concurso.new
    @accion = { action: 'create', method: 'post' }
  end

  def create
    @consurso = Concurso.new(concurso_params)
    @consurso.user_id = current_user.email
    @consurso.concurso_id = generate_concurso_uuid
    @consurso.concurso_url = @consurso.name.gsub(/[^a-zA-Z\d_\-]/, '-')
    parametros = params[:concurso]
    @consurso.startDate = date_from_params(parametros, 'startDate')
    @consurso.endDate = date_from_params(parametros, 'endDate')
    @consurso.image2 = upload_image_concurso(params[:concurso][:image2])
    # upload_image2
    if @consurso.save
      redirect_to concursos_path, notice: "El concurso #{@consurso.name} ha sido creado."
    else
      render 'new'
    end
  end

  def edit
    @consurso = Concurso.find(user_id: current_user.email,
                              concurso_id: params[:id])
    # @accion = { action: 'update', method: 'put' }
    @accion = { action: 'update', method: 'put' }
  end

  def update
    concurso_actual = Concurso.find(user_id: current_user.email, concurso_id:
        params[:id])

    if Concurso.update(concurso_params_update)
      delete_concurso_cache(concurso_actual.concurso_url)
      redirect_to concursos_path
    else
      render 'edit'
    end
  end

  def destroy
    @concurso = Concurso.find(user_id: current_user.email,
                              concurso_id: params[:id])
    delete_image_concurso(@concurso)
    delete_concurso_cache(@concurso.concurso_url)
    @concurso.delete!

    redirect_to concursos_path
  end

  private

  def concurso_params_update
    if  params[:concurso][:image2].nil? ||
        params[:concurso][:image2].original_filename == ''
      { user_id: current_user.email, concurso_id: params[:id],
        startDate: date_from_params(params[:concurso], 'startDate'),
        endDate: date_from_params(params[:concurso], 'endDate'),
        name: params[:concurso][:name],
        description: params[:concurso][:description] }
    else
      { user_id: current_user.email, concurso_id: params[:id],
        image2: upload_image_concurso(params[:concurso][:image2]),
        startDate: date_from_params(params[:concurso], 'startDate'),
        endDate: date_from_params(params[:concurso], 'endDate'),
        name: params[:concurso][:name],
        description: params[:concurso][:description] }

    end
  end

  def concurso_params
    params.require(:concurso).permit(:name, :description, :image2)
  end

end
