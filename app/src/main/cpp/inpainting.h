#ifndef IIAUAPP_INPAINTING_H
#define IIAUAPP_INPAINTING_H
#include <string>

using std::string;

namespace IIAUAPP {
    class INPAINTING {
    public:
        INPAINTING(string model_path = "det1.mnn");

        string inpainting(string img_path, string mask_path);

        string recover(string src_img_path, string inpainting_img_path , string mask_path);


    private:
        string model_path;

    };

}

#endif //IIAUAPP_INPAINTING_H
