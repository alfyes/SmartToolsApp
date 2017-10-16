require 'securerandom'
module ConcursosHelper
  def url_imagen_concurso(concurso)
    '/system/concursos/' + concurso.image2
  end

  def get_image_name(original)
    nombre_imagen = original
    while File.exist?(Rails.root.join('public', 'system', 'concursos',
                                      nombre_imagen))
      pos_punto = nombre_imagen.rindex('.')
      nombre_imagen = Random.rand(1000).to_s.prepend(nombre_imagen[0, pos_punto] + '_') + nombre_imagen[pos_punto, nombre_imagen.length]
    end
    nombre_imagen
  end

  def delete_image_concurso(concurso)
    if File.exist?(Rails.root.join('public', 'system', 'concursos',
                                   concurso.image2))
      File.delete(Rails.root.join('public', 'system', 'concursos',
                                  concurso.image2))
    end
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
end
