// 把图片转成base64编码
export function getBase64(file) {
  return new Promise(function (resolve, reject) {
    let reader = new FileReader();
    let imgResult = "";
    reader.readAsDataURL(file);
    reader.onload = function () {
      imgResult = reader.result;
    };
    reader.onerror = function (error) {
      reject(error);
    };
    reader.onloadend = function () {
      let item = ";base64,";
      imgResult = imgResult.substring(imgResult.indexOf(item) + item.length);
      resolve(imgResult);
    }
  })
}

// 获取文件后缀
export function getFileType(fileName) {
  let spotIndex = fileName.lastIndexOf(".");
  let fileLength = fileName.length;
  let fileType = fileName.substring(spotIndex, fileLength);
  return fileType;
}
