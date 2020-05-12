import React, {Component} from 'react'
import classNames from 'classnames'
import {
  Alert,
  Card,
  CardBody,
  CardHeader,
  FormGroup,
  Input,
  InputGroup,
  InputGroupAddon,
  InputGroupText,
  Nav,
  NavItem,
  NavLink,
  Popover,
  PopoverBody,
  TabContent,
  TabPane,
} from 'reactstrap'
import {withTranslation} from 'react-i18next'
import {FaQuestionCircle} from 'react-icons/fa'

import FileUploadForm from '../../FileUploadForm'
import {uploadFile} from '../../../api'

class ImageSelector extends Component {
  constructor(props) {
    super(props)
    this.state = {
      activeTab: 'upload',
      progress: 0,
      tooltipsOpen: {
        label: false,
        upload: false,
        url: false,
      },
      showUploadSuccess: false,
      tabToggleEnabled: true,
    }
  }

  toggleTab = (activeTab, evt) => {
    if (
      activeTab !== this.state.activeTab &&
      evt.currentTarget === evt.target
    ) {
      this.props.resetFileResource()
      this.setState({
        activeTab,
      })
    }
  }

  toggleTooltip = (name) => {
    this.setState({
      tooltipsOpen: {
        ...this.state.tooltipsOpen,
        [name]: !this.state.tooltipsOpen[name],
      },
    })
  }

  updateProgress = (progress) => {
    this.setState({
      progress,
    })
  }

  uploadImage = async (image) => {
    const {apiContextPath, onChange} = this.props
    this.setState({
      tabToggleEnabled: false,
    })
    const response = await uploadFile(
      apiContextPath,
      image,
      this.updateProgress
    )
    const responseJson = JSON.parse(response)
    this.setState({
      tabToggleEnabled: true,
      showUploadSuccess: true,
    })
    setTimeout(() => this.setState({showUploadSuccess: false}), 3000)
    onChange({
      ...responseJson,
      uri: `${responseJson.iiifBaseUrl}/full/full/0/default.${responseJson.filenameExtension}`,
    })
  }

  render() {
    const {fileResource, onChange, t} = this.props
    return (
      <Card className="mt-0">
        <CardHeader className="font-weight-bold">
          <Nav className="card-header-tabs" tabs>
            <NavItem>
              <NavLink
                className={classNames({
                  active: this.state.activeTab === 'upload',
                })}
                disabled={!this.state.tabToggleEnabled}
                href="#"
                onClick={(evt) => this.toggleTab('upload', evt)}
              >
                {t('selectImage.useUpload')}
                <FaQuestionCircle
                  className="ml-1"
                  id="upload-tooltip"
                  style={{cursor: 'pointer'}}
                />
                <Popover
                  isOpen={this.state.tooltipsOpen.upload}
                  placement="top"
                  target="upload-tooltip"
                  toggle={() => this.toggleTooltip('upload')}
                >
                  <PopoverBody>{t('tooltips.upload')}</PopoverBody>
                </Popover>
              </NavLink>
            </NavItem>
            <NavItem>
              <NavLink
                className={classNames({active: this.state.activeTab === 'url'})}
                disabled={!this.state.tabToggleEnabled}
                href="#"
                onClick={(evt) => this.toggleTab('url', evt)}
              >
                {t('selectImage.useUrl')}
                <FaQuestionCircle
                  className="ml-1"
                  id="url-tooltip"
                  style={{cursor: 'pointer'}}
                />
                <Popover
                  isOpen={this.state.tooltipsOpen.url}
                  placement="top"
                  target="url-tooltip"
                  toggle={() => this.toggleTooltip('url')}
                >
                  <PopoverBody>{t('tooltips.url')}</PopoverBody>
                </Popover>
              </NavLink>
            </NavItem>
          </Nav>
        </CardHeader>
        <CardBody className="text-center">
          <TabContent activeTab={this.state.activeTab} className="border-0 p-0">
            <TabPane tabId="upload">
              <Alert color="success" isOpen={this.state.showUploadSuccess}>
                {t('selectImage.uploadSuccessful')}
              </Alert>
              <FileUploadForm
                onChange={(file) => this.uploadImage(file)}
                progress={this.state.progress}
              />
            </TabPane>
            <TabPane tabId="url">
              <FormGroup>
                <Input
                  name="url"
                  onChange={(evt) => onChange({uri: evt.target.value})}
                  placeholder="URL"
                  required
                  type="url"
                  value={fileResource.uri}
                />
              </FormGroup>
              <FormGroup className="mb-0">
                <InputGroup>
                  <Input
                    name="label"
                    onChange={(evt) =>
                      onChange({
                        label: {
                          [Object.keys(fileResource.label)[0]]:
                            evt.target.value,
                        },
                      })
                    }
                    placeholder={t('label')}
                    required
                    type="text"
                    value={
                      fileResource.label
                        ? Object.values(fileResource.label)[0]
                        : ''
                    }
                  />
                  <InputGroupAddon addonType="append">
                    <InputGroupText>
                      <FaQuestionCircle
                        id="label-tooltip"
                        style={{cursor: 'pointer'}}
                      />
                      <Popover
                        isOpen={this.state.tooltipsOpen.label}
                        placement="left"
                        target="label-tooltip"
                        toggle={() => this.toggleTooltip('label')}
                      >
                        <PopoverBody>{t('tooltips.label')}</PopoverBody>
                      </Popover>
                    </InputGroupText>
                  </InputGroupAddon>
                </InputGroup>
              </FormGroup>
            </TabPane>
          </TabContent>
        </CardBody>
      </Card>
    )
  }
}

export default withTranslation()(ImageSelector)
