require 'aws-sdk-s3'
module ApplicationHelper

  def upload_file_s3(uploaded_io, folder)

    s3 = Aws::S3::Resource.new
    bucket = s3.bucket('smart-tools-app')
    begin
      file_name = get_new_file_name(uploaded_io.original_filename)
      obj = bucket.object(folder + file_name)
    end while obj.exists?

    obj.put(acl: 'public-read', body: uploaded_io.read)
    file_name
  end

  def delete_file_s3(folder, file_name)
    s3 = Aws::S3::Resource.new
    bucket = s3.bucket('smart-tools-app')
    obj = bucket.object(folder + file_name)
    obj.delete
  end

  def get_new_file_name(original)
    file_name = original

    pos_punto = file_name.rindex('.')
    Random.rand(100_000).to_s.prepend(file_name[0, pos_punto] + '_') + file_name[pos_punto, file_name.length]
  end
end
