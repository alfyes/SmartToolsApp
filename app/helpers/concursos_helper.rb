require 'securerandom'

module ConcursosHelper
  include ApplicationHelper

  def url_imagen_concurso(concurso)
    root_url_content + 'concursos/' + concurso.image2
  end

  def delete_image_concurso(concurso)
    delete_file_s3('concursos/', concurso.image2)
  end

  def concurso_path(concurso)
    root_url + 'concursos/' + concurso.concurso_id
  end

  def edit_concurso_path(concurso)
    root_url + 'concursos/' + concurso.concurso_id + '/edit'
  end

  def generate_concurso_uuid
    SecureRandom.uuid
  end

  def date_from_params(params, field)
    Date.new(params[field + '(1i)'].to_i,
             params[field + '(2i)'].to_i,
             params[field + '(3i)'].to_i)
  end

  def upload_image_concurso(uploaded_io)
    upload_file_s3(uploaded_io, 'concursos/')
  end

  def delete_concurso_cache(url_concurso)
    Rails.cache.delete(url_concurso)
  end
end
