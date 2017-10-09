class ConcursosController < ApplicationController
  include ConcursosHelper
  def index
    # @concursos = current_user.concursos.reverse_order.paginate(:page => params[:page], :per_page => 4)
    @concursos = Concurso.query(key_condition_expression: 'user_id = :h',
                                expression_attribute_values: { ':h' => current_user.email })
    print(@concursos.count)
    esto = 'hola'
  end

  def show
    @concurso = current_user.concursos.find(params[:id])
    @videos = @concurso.videos.reverse_order.paginate(page: params[:page], per_page: 4)
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
    @consurso.url = @consurso.name.gsub(/[^a-zA-Z\d_\-]/, '-')
    parametros = params[:concurso]
    @consurso.startDate = date_from_params(parametros, 'startDate')
    @consurso.endDate = date_from_params(parametros, 'endDate')
    @consurso.image2 = upload_image
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
    if Concurso.update(concurso_params_update)
      redirect_to concursos_path
    else
      render 'edit'
    end
  end

  def destroy
    @concurso = Concurso.find(user_id: current_user.email,
                              concurso_id: params[:id])
    delete_image_concurso(@concurso)
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
        image2: upload_image,
        startDate: date_from_params(params[:concurso], 'startDate'),
        endDate: date_from_params(params[:concurso], 'endDate'),
        name: params[:concurso][:name],
        description: params[:concurso][:description] }

    end
  end

  def concurso_params
    params.require(:concurso).permit(:name, :description, :image2)
  end

  def upload_image
    uploaded_io = params[:concurso][:image2]

    nombre_imagen = get_image_name(uploaded_io.original_filename)

    File.open(Rails.root.join('public', 'system', 'concursos',
                              nombre_imagen), 'wb') do |file|
      file.write(uploaded_io.read)
    end
    nombre_imagen
  end
end
