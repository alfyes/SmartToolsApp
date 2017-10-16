require 'securerandom'
module ContestsHelper

  def url_video_original(video)
    '/system/videos/original/' + video.fileName
  end

  def url_video_convertido(video)
    if video.fileNameConv.nil?
      '/system/videos/convertido/' + 'Error'
    else
      '/system/videos/convertido/' + video.fileNameConv
    end
  end

  def get_video_name(original)
    nombre_video = original
    while File.exist?(Rails.root.join('public', 'system', 'videos', 'original',
                                      nombre_video))
      pos_punto = nombre_video.rindex('.')
      nombre_video = Random.rand(1000).to_s.prepend(nombre_video[0, pos_punto] + '_') + nombre_video[pos_punto, nombre_video.length]
    end
    nombre_video
  end

  def generate_video_uuid
    SecureRandom.uuid
  end

  def delete_video(video)
    nombre_archivo = Rails.root.join('public', 'system', 'videos', 'original',
                                     video.fileName)
    if File.exist?(nombre_archivo)
      File.delete(nombre_archivo)
    end
    nombre_archivo = Rails.root.join('public', 'system', 'videos', 'convertido',
                                     video.fileNameConv)
    if File.exist?(nombre_archivo)
      File.delete(nombre_archivo)
    end
  end
end
